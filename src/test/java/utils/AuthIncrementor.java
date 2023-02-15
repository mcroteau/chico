package utils;

public class AuthIncrementor {
    private int count;

    public AuthIncrementor(){
        this.count = 0;
    }

    public void increment(){
        count++;
    }

    public int getCount(){
        return this.count;
    }
}