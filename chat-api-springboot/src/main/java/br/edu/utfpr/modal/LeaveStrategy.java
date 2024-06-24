package br.edu.utfpr.modal;

public enum LeaveStrategy {
    DELETE_GROUP(0),
    ASSIGN_NEW_ADMIN(1);

    private final int strategyCode;

    LeaveStrategy(int strategyCode) {
        this.strategyCode = strategyCode;
    }

    public int getStrategyCode() {
        return strategyCode;
    }
}
