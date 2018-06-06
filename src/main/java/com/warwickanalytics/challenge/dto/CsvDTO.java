package com.warwickanalytics.challenge.dto;

public class CsvDTO {

    private Integer id;
    private Integer var1;
    private Integer var2;
    private Integer var3;
    private Integer var4;
    private Integer var5;
    private Integer decision;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVar1() {
        return var1;
    }

    public void setVar1(Integer var1) {
        this.var1 = var1;
    }

    public Integer getVar2() {
        return var2;
    }

    public void setVar2(Integer var2) {
        this.var2 = var2;
    }

    public Integer getDecision() {
        return decision;
    }

    public Integer getVar3() {
        return var3;
    }

    public void setVar3(Integer var3) {
        this.var3 = var3;
    }

    public Integer getVar4() {
        return var4;
    }

    public void setVar4(Integer var4) {
        this.var4 = var4;
    }

    public Integer getVar5() {
        return var5;
    }

    public void setVar5(Integer var5) {
        this.var5 = var5;
    }

    public void setDecision(Integer decision) {
        this.decision = decision;
    }



    @Override
    public String toString() {
        return "CsvDTO{" +
                "id='" + id + '\'' +
                ", var1='" + var1 + '\'' +
                ", var2='" + var2 + '\'' +
                ", var3='" + var3 + '\'' +
                ", var4='" + var4 + '\'' +
                ", var5='" + var5 + '\'' +
                ", decision='" + decision + '\'' +
                '}';
    }
}
