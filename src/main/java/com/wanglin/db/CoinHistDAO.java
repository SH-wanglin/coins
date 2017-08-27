package com.wanglin.db;


import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface CoinHistDAO {

    @SqlUpdate("insert into coin_hist (type_id, from_user, to_user, coin_num, created_on, updated_on) "
             + "values (:type_id, :from_user, :to_user, :coin_num, :time_stamp, :time_stamp)")
    void insert(@Bind("type_id") int type,@Bind("from_user") Long fromUser, @Bind("to_user") Long toUser,
                @Bind("coin_num") long coinNum, @Bind("time_stamp") long timeStamp);
}
