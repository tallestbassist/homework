import java.util.Objects;
class Match {
    private final String UUID;
    private final String returnRateA;
    private final String returnRateB;
    private final String matchResult;
    public Match(String uuid, String returnRateA, String returnRateB, String matchResult) {
        this.UUID = uuid;
        this.returnRateA = returnRateA;
        this.returnRateB = returnRateB;
        this.matchResult = matchResult;
    }

    public String getUuid() {
        return UUID;
    }
    public Float getReturnRate() {
        if (Objects.equals(matchResult, "A")) {
            return Float.parseFloat(this.returnRateA);
        } else {
            return Float.parseFloat(this.returnRateB);
        }
    }
    public String getMatchResult(){
        return matchResult;
    }
}
