package lol.svn.reward.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import lol.svn.reward.parser.RewardParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class ParserTest {

    @Test
    void parser () throws IOException {

        String html = new String(
                Objects.requireNonNull(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("parser/test1.html")
                ).readAllBytes(),
                StandardCharsets.UTF_8
        );

        var result = RewardParser.parse(html);
        assertEquals(result.getId(), "aaaaaaaa");
        assertEquals(result.getSecurityToken(),"aaaaaaaa-bbbbBBBbbbb-cccccCCCCCccccc");

        assertEquals(result.getStreakInfo().getScore(), 1234);

        var options = result.getOptions();
        assertNull(options.get(0).getGameType());

    }

}
