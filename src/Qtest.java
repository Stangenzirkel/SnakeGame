import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Author: Yuri Buyanov
 * Date: 24/05/2021 19:24
 */
public class Qtest {
    public static void main(String[] args) {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(9);
        q.add(10);
        q.add(11);

        for (int i = 0; i < 6; i++) {
            q.add(i);
            System.out.println(q.removeFirst());
        }

        System.out.println(q.getLast());
    }
}
