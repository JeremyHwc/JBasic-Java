package com.basic.condition_nest.strategy_mode;

@SuppressWarnings("ALL")
public enum SalaryStrategyEnum {

    BOSS(0) {
        @Override
        double getSalary() {
            return 100000;
        }
    },
    LEADER(1) {
        @Override
        double getSalary() {
            return 50000;
        }
    },
    STAFF(2) {
        @Override
        double getSalary() {
            return 10000;
        }
    };

    private final int position;

    SalaryStrategyEnum(int position) {
        this.position = position;
    }

    public static SalaryStrategyEnum valueOf(int position) {
        for (SalaryStrategyEnum salaryStrategyEnum : SalaryStrategyEnum.values()) {
            if (salaryStrategyEnum.position == position) {
                return salaryStrategyEnum;
            }
        }
        return null;
    }

    abstract double getSalary();
}