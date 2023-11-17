class Casino {
    private int balance;
    public Casino(){
        this.balance = 0;
    }
    public int getBalance(){
        return this.balance;
    }
    public void addBalance(int coin) {
        this.balance += coin;
    }
    public void removeBalance(int coin) {
        this.balance -= coin;
    }
}