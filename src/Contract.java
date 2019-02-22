/**
 * Simulates a Contract for each player
 */
public class Contract {

    private Integer salary;
    private Integer length;

    /**
     * Contract constructor
     * @param salary Salary of this contract
     * @param length Length of this contract
     */
    Contract(Integer salary, Integer length) {

        this.salary = salary;
        this.length = length;
    }


    /**
     * Salary getter
     * @return Salary of this contract
     */
    public Integer getSalary() {
        return this.salary;
    }


    /**
     * Length getter
     * @return Length of this contract
     */
    public Integer getLength() {
        return this.length;
    }


    /**
     * Sets the salary
     */
    public void setSalary(Integer salary) {
        this.salary = salary;
    }


    /**
     * Sets the length
     */
    public void setLength(Integer length) {
        this.length = length;
    }


    /**
     * Changes this player's contract by -1
     */
    public void decrementContractLength(){
        this.length--;
    }
}
