package com.wanglin.resources;

import com.wanglin.api.UserCoinInfo;
import com.wanglin.db.CoinHistDAO;
import com.wanglin.db.UserCoinDAO;
import com.wanglin.model.UserCoin;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CoinsResourceTest {
    private static final DBI myDbi = mock(DBI.class);
    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addResource(new CoinsResource(myDbi))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .build();
    private UserCoin userCoin;

    @Before
    public void setup() {
        userCoin = new UserCoin();
        userCoin.setUserId(1L);
        userCoin.setCoins(10L);
    }

    @After
    public void tearDown() {
        reset(myDbi);
    }

    @Test
    public void getUserCoinSuccess() {
        UserCoinDAO DAO = mock(UserCoinDAO.class);
        when(myDbi.onDemand(UserCoinDAO.class)).thenReturn(DAO);
        when(DAO.findByUserId(1L)).thenReturn(userCoin);

        UserCoinInfo found = RULE.target("/coins/user/1").request().get(UserCoinInfo.class);

        assertThat(found.getUserId()).isEqualTo(userCoin.getUserId());
        verify(DAO).findByUserId(1L);
    }

    @Test
    public void getUserCoinNotFound() {
        UserCoinDAO DAO = mock(UserCoinDAO.class);
        when(myDbi.onDemand(UserCoinDAO.class)).thenReturn(DAO);
        when(DAO.findByUserId(2L)).thenReturn(null);
        final Response response = RULE.target("/coins/user/2").request().get();

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        verify(DAO).findByUserId(2L);
    }

    @Test
    public void addUserCoinExist() {
        Handle handle = mock(Handle.class);
        when(myDbi.open()).thenReturn(handle);

        UserCoinDAO userCoinDAOW = mock(UserCoinDAO.class);
        when(handle.attach(UserCoinDAO.class)).thenReturn(userCoinDAOW);

        CoinHistDAO coinHistDAOW = mock(CoinHistDAO.class);
        when(handle.attach(CoinHistDAO.class)).thenReturn(coinHistDAOW);

        when(userCoinDAOW.findByUserIdWithLock(1L)).thenReturn(userCoin);
        UserCoinInfo found = RULE.target("/user/add")
                                 .queryParam("user_id", 1)
                                 .queryParam("coins", 10)
                                 .request().post(null, UserCoinInfo.class);

        assertThat(found.getUserId()).isEqualTo(userCoin.getUserId());
        assertThat(found.getCoins()).isEqualTo(userCoin.getCoins() + 10L);
        verify(userCoinDAOW).findByUserIdWithLock(1L);
    }

    @Test
    public void addUserCoinNotExist() {
        Handle handle = mock(Handle.class);
        when(myDbi.open()).thenReturn(handle);

        UserCoinDAO userCoinDAOW = mock(UserCoinDAO.class);
        when(handle.attach(UserCoinDAO.class)).thenReturn(userCoinDAOW);

        CoinHistDAO coinHistDAOW = mock(CoinHistDAO.class);
        when(handle.attach(CoinHistDAO.class)).thenReturn(coinHistDAOW);

        when(userCoinDAOW.findByUserIdWithLock(2L)).thenReturn(null);
        UserCoinInfo found = RULE.target("/user/add")
                .queryParam("user_id", 2)
                .queryParam("coins", 10)
                .request().post(null, UserCoinInfo.class);

        assertThat(found.getUserId()).isEqualTo(2L);
        assertThat(found.getCoins()).isEqualTo(10L);
        verify(userCoinDAOW).findByUserIdWithLock(2L);
    }
}
