import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 30.08.2016.
 */
public class Bill implements Serializable {
    private String name;
    private LocalDate date;
    private List<Purchase> list;

    Bill(String name, LocalDate date, List list) {
        this.name = name;
        this.date = date;
        this.list = list;
    }

    @Override
    public String toString() {
        String result = name+" "+date+" "+list;
        return result;
    }
}
