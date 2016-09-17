import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


public class Bill implements Serializable {
    private String name;
    private LocalDate date;
    private List<Purchase> list;

    Bill(String name, LocalDate date, List list) {
        this.name = name;
        this.date = date;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Purchase> getList() {
        return list;
    }

    public void setList(List<Purchase> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        String result = name+" "+date+" "+list;
        return result;
    }
}

//Bill bill = new Bill("Name",