package com.zdww.mylibrary.model;

import java.io.Serializable;
import java.util.List;

public class MultiSelectedBean implements Serializable {

    private String msg;
    private Integer code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private String dictId;
        private String dictName;
        private String dictValue;
        private String dictTypeId;
        private Object dictUnit;
        private Object dictExtend;
        private Object validates;
        private Double sort;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getDictId() {
            return dictId;
        }

        public void setDictId(String dictId) {
            this.dictId = dictId;
        }

        public String getDictName() {
            return dictName;
        }

        public void setDictName(String dictName) {
            this.dictName = dictName;
        }

        public String getDictValue() {
            return dictValue;
        }

        public void setDictValue(String dictValue) {
            this.dictValue = dictValue;
        }

        public String getDictTypeId() {
            return dictTypeId;
        }

        public void setDictTypeId(String dictTypeId) {
            this.dictTypeId = dictTypeId;
        }

        public Object getDictUnit() {
            return dictUnit;
        }

        public void setDictUnit(Object dictUnit) {
            this.dictUnit = dictUnit;
        }

        public Object getDictExtend() {
            return dictExtend;
        }

        public void setDictExtend(Object dictExtend) {
            this.dictExtend = dictExtend;
        }

        public Object getValidates() {
            return validates;
        }

        public void setValidates(Object validates) {
            this.validates = validates;
        }

        public Double getSort() {
            return sort;
        }

        public void setSort(Double sort) {
            this.sort = sort;
        }
    }
}
