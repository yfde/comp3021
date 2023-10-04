package hk.ust.cse.comp3021.lab3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

/**
 * {@link Bus} class represents a bus that  can be added to the instance of {@link BusCompany} via {@link BusCompany#createAndAddBus(int, String)}.
 */
record Bus(int id, String model) {
    // TODO implement this Record
}

/**
 * {@link BusCompany} represents a bus company class, whose instance can possess a list of {@link Bus} instances.
 * {@link BusCompany} should also keep tracking the total number of {@link BusCompany} instances that have ever been created (those that have been garbage-collected should also be counted).
 */
public class BusCompany {
    // TODO add any static or instance field as you need.
    private static int numCompanies = 0;
    { numCompanies += 1; }
    private String name;
    private ArrayList<Bus> bus = new ArrayList<>();


    /**
     * Initialize a new BusCompany instance.
     *
     * @param name the name of the bus company.
     */
    public BusCompany(@NotNull String name) {
        // TODO implement this constructor
        this.name = name;
    }

    /**
     * @return the name of the bus company.
     */
    @NotNull
    public String getName() {
        // TODO implement this method
        return this.name;
        // throw new RuntimeException("implement me");
    }

    /**
     * @return the number of {@link Bus} instances that current bus company has.
     */
    public int getNumBuses() {
        // TODO implement this method
        return this.bus.size();
        // throw new RuntimeException("implement me");
    }

    /**
     * @param id the id of the bus instance in current bus company.
     * @return the {@link Bus} instance with the {@code id} in the current bus company. Return null if not exist.
     */
    @Nullable
    public Bus getBusByID(int id) {
        // TODO implement this method
        for(Bus i:this.bus){
            if(i.id() == id){
                return i;
            }
        }
        return null;
        // throw new RuntimeException("implement me");
    }

    /**
     * @return an array of unique models used in current bus company. There should not be two equivalent models in the returned array.
     * If there are no {@link Bus} instances in the current bus company, return an empty array with length 0.
     */
    @NotNull
    public String[] getModels() {
        // TODO implement this method
        ArrayList<String> uniqueList = new ArrayList<>();
        for(Bus i:this.bus){
            boolean modelExist = false;
            for(String j:uniqueList){
                if(i.model().equals(j)){
                    modelExist = true;
                }
            }
            if(!modelExist){
                uniqueList.add(i.model());
            }
        }
        String[] result = new String[uniqueList.size()];
        return uniqueList.toArray(result);
        // throw new RuntimeException("implement me");
    }

    /**
     * Create a new {@link Bus} instance and add to the current bus company.
     * Each {@link Bus} instance added to the {@link BusCompany} instance should have unique {@link Bus} id.
     * If the given id already exists in the current {@link BusCompany} instance, do not add the new {@link Bus} instance and return false.
     * Otherwise, if a new {@link Bus} instance is successfully added, return true.
     *
     * @param id    the id of the bus to be created.
     * @param model the model of the bus to be created.
     * @return whether a new bus instance is added to the bus company.
     */
    public boolean createAndAddBus(int id, String model) {
        // TODO implement this method
        boolean idExist = false;
        for(Bus i:this.bus){
            if(i.id() == id){
                idExist = true;
            }
        }
        if(idExist){
            return false;
        }
        this.bus.add(new Bus(id, model));
        return true;
        // throw new RuntimeException("implement me");
    }

    /**
     * Remove all {@link Bus} instances that have been added to the current bus company via {@link BusCompany#createAndAddBus(int, String)}.
     */
    public void removeAllBuses() {
        // TODO implement this method
        while(!this.bus.isEmpty()){
            this.bus.remove(0);
        }
        // throw new RuntimeException("implement me");
    }

    /**
     * @return the total number of {@link BusCompany} instances that have been created.
     */
    public static int getNumCompanies() {
        // TODO implement this method
        return numCompanies;
        // throw new RuntimeException("implement me");
    }

}

