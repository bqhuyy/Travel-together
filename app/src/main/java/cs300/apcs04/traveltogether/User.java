package cs300.apcs04.traveltogether;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String name;
    private String email;
    private HashMap<String, String> mPlans;

    public User() {
        name = "";
        email = "";
    }
    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.mPlans = null;
    }

	public HashMap<String, String> getmPlans() {
		return mPlans;
	}

	public void setmPlans(HashMap<String, String> plans){
    	this.mPlans = plans;
	}

	public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
