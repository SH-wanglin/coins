package com.wanglin.mapper;


import com.wanglin.model.UserCoin;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCoinMapper implements ResultSetMapper<UserCoin> {
    public UserCoin map (int index, ResultSet r, StatementContext ctx) throws SQLException
    {
        return new UserCoin(r.getInt("user_id"), r.getInt("coins"));
    }
}
