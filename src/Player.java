import java.util.Objects;

class Player {
    private final String UUID;
    private int coin;
    private int coinWins;
    private int coinLosses;
    private int placedBets;
    private int wonBets;
    private boolean madeIllegalMove;
    private String illegalMove;

    public Player(String uuid) {
        this.UUID = uuid;
        this.coin = 0;
        this.coinWins = 0;
        this.coinLosses = 0;
        this.madeIllegalMove = false;
        this.illegalMove = "";
        this.placedBets = 0;
        this.wonBets = 0;
    }

    public void placedABet(){
        this.placedBets += 1;
    }
    public void wonABet(){
        this.wonBets += 1;
    }
    public float getWinRate(){
        return (float) wonBets /placedBets;
    }
    public void setFirstIllegalMove(String operation, String matchID, int coinAmount, String bet){
        if(Objects.equals(matchID, "")) {
            matchID = "null";
        }

        if(Objects.equals(bet, "")) {
            bet = "null";
        }

        if(Objects.equals(illegalMove, "")){
            this.illegalMove = this.UUID + " " + operation + " " + matchID + " " + coinAmount + " " + bet;
        }
    }
    public String getIllegalMove() {
        return this.illegalMove;
    }

    public void setIllegalMoveStatusToTrue() {
        this.madeIllegalMove = true;
    }
    public String getUUID() {
        return UUID;
    }
    public int getCoin() {
        return coin;
    }
    public void addCoin(Float coin) {
        this.coin += coin;
    }
    public void removeCoin(Float coin){
        this.coin -= coin;
    }
    public void addCoinWins(float coin) {
        this.coinWins += (int) coin;
    }
    public void addCoinLosses(float coin) {
        this.coinLosses += (int) coin;
    }
    public int getWins() {
        return this.coinWins;
    }
    public int getLosses(){
        return this.coinLosses;
    }
    public boolean checkIfMadeIllegalMove(){
        return this.madeIllegalMove;
    }
}