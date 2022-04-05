package Lada303.dao;

import Lada303.models.Step;

import java.util.List;

public interface StepDAO {
    void addNewStep(int id_gameplay, Step step);
    List<Step> getAllStepGameplay(int id_gameplay);
}
