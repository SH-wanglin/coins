package com.wanglin.resources;

import com.codahale.metrics.annotation.Timed;
import com.wanglin.api.TransactionResult;
import com.wanglin.api.UserCoinInfo;
import com.wanglin.constant.CoinType;
import com.wanglin.db.CoinHistDAO;
import com.wanglin.db.UserCoinDAO;
import com.wanglin.model.UserCoin;
import io.dropwizard.jersey.params.LongParam;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class CoinsResource {
    private final DBI myDbi;

    public CoinsResource(DBI myDbi) {
        this.myDbi = myDbi;
    }


    @GET
    @Path("/coins/user/{id}")
    @Timed
    public UserCoinInfo getUserCoinInfo(@PathParam("id") LongParam userId){
        UserCoinDAO dao = myDbi.onDemand(UserCoinDAO.class);
        UserCoin userCoin = dao.findByUserId(userId.get());
        if(userCoin == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        UserCoinInfo uci = new UserCoinInfo(userId.get(), userCoin.getCoins());
        return uci;
    }

    @POST
    @Path("/user/add")
    @Timed
    public UserCoinInfo addUserCoin(@QueryParam("user_id")  LongParam userId,
                                    @QueryParam("coins") LongParam incrCoins){
        if(userId == null || incrCoins == null) {
            throw new WebApplicationException("you need complete params");
        }
        Long nowTimeStamp = System.currentTimeMillis() / 1000;
        Long newCoins = incrCoins.get();
        Handle handle = myDbi.open();
        handle.begin();
        try {
            UserCoinDAO userCoinDAOW = handle.attach(UserCoinDAO.class);
            CoinHistDAO coinHistDAOW = handle.attach(CoinHistDAO.class);
            UserCoin oldUserCoin = userCoinDAOW.findByUserIdWithLock(userId.get());
            if (oldUserCoin == null) {
                userCoinDAOW.insertUserCoin(userId.get(), newCoins, nowTimeStamp);
            } else {
                newCoins = oldUserCoin.getCoins() + incrCoins.get();
                userCoinDAOW.updateUserCoin(userId.get(), newCoins, nowTimeStamp);
            }

            coinHistDAOW.insert(CoinType.CHARGE.getTypeId(), null, userId.get(),
                                newCoins, nowTimeStamp);
            handle.commit();
            handle.close();
        } catch (Exception e) {
            handle.rollback();
            handle.close();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
        UserCoinInfo uci = new UserCoinInfo(userId.get(), newCoins);
        return uci;
    }

    @POST
    @Path("/transaction/transfer")
    @Timed
    public TransactionResult addUserCoin(@QueryParam("from_user_id") LongParam fromUserId,
                                         @QueryParam("to_user_id") LongParam toUserId,
                                         @QueryParam("coins") LongParam coins){
        if(fromUserId == null || toUserId == null || coins == null) {
            throw new WebApplicationException("you need complete params");
        }
        Long nowTimeStamp = System.currentTimeMillis() / 1000;
        Handle handle = myDbi.open();
        handle.begin();
        TransactionResult tr = null;
        try {
            UserCoinDAO userCoinDAOW = handle.attach(UserCoinDAO.class);
            CoinHistDAO coinHistDAOW = handle.attach(CoinHistDAO.class);
            UserCoin oldFromUserCoin = userCoinDAOW.findByUserIdWithLock(fromUserId.get());
            UserCoin oldToUserCoin = userCoinDAOW.findByUserIdWithLock(toUserId.get());

            Long transactionCoins = coins.get();

            if(oldFromUserCoin == null){
                throw new WebApplicationException("from user is not exist");
            }

            if (oldFromUserCoin.getCoins() < transactionCoins) {
                throw new WebApplicationException("from user has no enough balance");
            } else {
                Long newFromUserCoinNum = oldFromUserCoin.getCoins() - transactionCoins;
                userCoinDAOW.updateUserCoin(fromUserId.get(), newFromUserCoinNum, nowTimeStamp);
                Long newToUserCoinNum = transactionCoins;
                if(oldFromUserCoin == null) {
                    userCoinDAOW.insertUserCoin(toUserId.get(), newToUserCoinNum, nowTimeStamp);
                } else {
                    newToUserCoinNum = oldToUserCoin.getCoins() + transactionCoins;
                    userCoinDAOW.updateUserCoin(toUserId.get(), newToUserCoinNum, nowTimeStamp);
                }

                coinHistDAOW.insert(CoinType.TRANSFER.getTypeId(), fromUserId.get(), toUserId.get(),
                                    transactionCoins, nowTimeStamp);
                UserCoinInfo newFromUserCoin = new UserCoinInfo(fromUserId.get(), newFromUserCoinNum);
                UserCoinInfo newToUserCoin   = new UserCoinInfo(toUserId.get(), newToUserCoinNum);
                tr = new TransactionResult(newFromUserCoin, newToUserCoin);
            }

            handle.commit();
            handle.close();
        } catch (Exception e) {
            handle.rollback();
            handle.close();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return tr;
    }
}
