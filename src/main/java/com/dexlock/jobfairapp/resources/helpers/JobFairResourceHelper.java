package com.dexlock.jobfairapp.resources.helpers;

import com.dexlock.jobfairapp.dao.OpeningDAO;
import com.dexlock.jobfairapp.models.Opening;

import java.util.ArrayList;

public class JobFairResourceHelper {
    OpeningDAO openingDAO = new OpeningDAO();

    public ArrayList<Opening> searchOpenings(Opening opening) {
        ArrayList<Opening> openings = openingDAO.searchOpenings(opening);
        return openings;
    }
}
