package apiTests.pojoClasses;

public class CreatorUser {

    private String name;
    private String job;

    public CreatorUser(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
