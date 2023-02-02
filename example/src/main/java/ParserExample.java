import lol.svn.reward.parser.RewardParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ParserExample {

    public static void main(String[] args) throws IOException {

        String html = new String(
                Objects.requireNonNull(
                        ParserExample.class.getResourceAsStream("parser_input.html")
                ).readAllBytes(),
                StandardCharsets.UTF_8
        );
        System.out.println(html);

        var parseResult = RewardParser.parse(html);
        System.out.println(parseResult.getSecurityToken());

    }

}
