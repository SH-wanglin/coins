package com.wanglin.api;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserCoinInfoTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        final UserCoinInfo userCoinInfo = new UserCoinInfo( 1, 1);

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/userCoinInfo.json"), UserCoinInfo.class));

        assertThat(MAPPER.writeValueAsString(userCoinInfo)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final UserCoinInfo userCoinInfo = new UserCoinInfo( 1, 1);
        final UserCoinInfo expected = MAPPER.readValue(fixture("fixtures/userCoinInfo.json"), UserCoinInfo.class);

        assertThat(expected.getUserId()).isEqualTo(userCoinInfo.getUserId());
        assertThat(expected.getCoins()).isEqualTo(userCoinInfo.getCoins());
    }
}
