public class Player implements Comparable<Player> {
    protected String name;
    protected int age;
    protected String country;
    protected int number;
    protected String position;
    protected int rating;


    public Player(String name, String country, int age, String position, int number, int rating) {
        this.name = name;
        this.age = age;
        this.country = country;
        this.number = number;
        this.position = position;
        this.rating = rating;

    }

    public int compareTo(Player player) {
        return Double.compare(this.getRating(), player.getRating());
    }

    public int getAge() {
        return age;
    }

    public int getNumber() {
        return number;
    }

    public String getPosition() {
        return position;
    }

    public String getCountry() {
        return country;
    }

    public int getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isSamePosition(Player player) {
        return this.position.equals(player.getPosition());
    }

    public String toString() {
        return name + " (" + number + ", " + position + ")";
    }


}
