import scotlandyard.ScotlandYard;

import java.util.ArrayList;
import java.util.List;

public class ValidMovesTests {

    TestHelper.TestPlayer player1;
    TestHelper.TestPlayer player2;
    TestHelper.TestPlayer player3;

    public static ScotlandYard simpleGame(int numDetectives) throws Exception {
        List<Boolean> rounds = new ArrayList<Boolean>();
        rounds.add(true);
        rounds.add(true);
        rounds.add(true);
        rounds.add(true);

        return TestHelper.getGame(numDetectives, rounds, "small_map.txt");
    }


}
