package com.wanglin.db;


import com.wanglin.mapper.UserCoinMapper;
import com.wanglin.model.UserCoin;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(UserCoinMapper.class)
public interface UserCoinDAO {
    @SqlUpdate("insert into user_coin (user_id, coins, created_on, updated_on) "
              +"values (:user_id, :coins, :time_stamp, :time_stamp)")
    void insertUserCoin(@Bind("user_id") long userId, @Bind("coins") long coins, @Bind("time_stamp") long timeStamp);

    @SqlUpdate("update user_coin set coins = :coins, updated_on = :time_stamp where user_id = :user_id")
    void updateUserCoin(@Bind("user_id") long userId, @Bind("coins") long coins, @Bind("time_stamp") long timeStamp);

    @SqlQuery("select user_id, coins from user_coin where user_id = :user_id")
    UserCoin findByUserId(@Bind("user_id") long userId);

    @SqlQuery("select user_id, coins from user_coin where user_id = :user_id for update")
    UserCoin findByUserIdWithLock(@Bind("user_id") long userId);
}
