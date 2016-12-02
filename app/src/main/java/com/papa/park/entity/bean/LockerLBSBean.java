package com.papa.park.entity.bean;

import java.util.List;

/**
 * User: PAPA
 * Date: 2016-12-01
 */

public class LockerLBSBean {


    /**
     * status : 0
     * total : 5
     * size : 4
     * contents : [{"firstHour":"1","startTime":"05 : 00","parkingNumber":"8888","remark":"好好干",
     * "contacts":"张虎","userAccount":"13682343850","phone":"13682343850","state":"2",
     * "endTime":"22 : 00","eachCharge":"5","eachHour":"1","releaseTitle":"明年","canRent":"1",
     * "tags":"车位","uid":1869342351,"province":"广东省","geotable_id":158209,"district":"宝安区",
     * "modify_time":1480084907,"create_time":1480003763,"firstStopPrice":"10",
     * "parkingLotId":"1869342342","rentalDate":"星期一","city":"深圳市","location":[113.853605,22
     * .606691],"address":"地下室","title":"固戍-地铁站","coord_type":3,"type":0,"distance":0,
     * "weight":0},{"firstHour":"1","startTime":"06 : 00","parkingNumber":"9999","remark":"便宜",
     * "contacts":"张虎","userAccount":"13682343850","phone":"13682343850","state":"3",
     * "endTime":"21 : 00","eachCharge":"10","eachHour":"2","releaseTitle":"出租","canRent":"1",
     * "tags":"车位","uid":1870143252,"province":"广东省","geotable_id":158209,"district":"宝安区",
     * "modify_time":1480084922,"create_time":1480081920,"firstStopPrice":"9",
     * "parkingLotId":"1870143242","rentalDate":"星期一、星期二","city":"深圳市","location":[113.856631,22
     * .602733],"address":"A区","title":"联升商务大厦","coord_type":3,"type":0,"distance":0,"weight":0},
     * {"firstHour":"1","startTime":"06 : 00","parkingNumber":"7777","remark":"方便",
     * "contacts":"张虎","userAccount":"13682343850","phone":"13682343850","state":"2",
     * "endTime":"23 : 00","eachCharge":"3","eachHour":"1","releaseTitle":"出租锁","canRent":"1",
     * "tags":"车位","uid":1870188256,"province":"广东省","geotable_id":158209,"district":"宝安区",
     * "modify_time":1480085461,"create_time":1480085249,"firstStopPrice":"5",
     * "parkingLotId":"1870143242","rentalDate":"星期二","city":"深圳市","location":[113.856623,22
     * .602735],"address":"负一楼","title":"联升商务大厦","coord_type":3,"type":0,"distance":0,
     * "weight":0},{"firstHour":"1","startTime":"06 : 00","parkingNumber":"6666","remark":"哈哈",
     * "contacts":"张虎","userAccount":"13682343850","phone":"13682343850","state":"3",
     * "endTime":"23 : 00","eachCharge":"5","eachHour":"1","releaseTitle":"出租车位","canRent":"1",
     * "tags":"车位","uid":1870189899,"province":"广东省","geotable_id":158209,"district":"宝安区",
     * "modify_time":1480085472,"create_time":1480085400,"firstStopPrice":"13",
     * "parkingLotId":"1870189888","rentalDate":"星期二、星期四、星期五","city":"深圳市","location":[113
     * .856654,22.602733],"address":"负一楼","title":"茶西经发工业园","coord_type":3,"type":0,"distance":0,
     * "weight":0}]
     */

    private int status;
    private int total;
    private int size;
    private List<ContentsBean> contents;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ContentsBean> getContents() {
        return contents;
    }

    public void setContents(List<ContentsBean> contents) {
        this.contents = contents;
    }

    public static class ContentsBean {
        /**
         * firstHour : 1
         * startTime : 05 : 00
         * parkingNumber : 8888
         * remark : 好好干
         * contacts : 张虎
         * userAccount : 13682343850
         * phone : 13682343850
         * state : 2
         * endTime : 22 : 00
         * eachCharge : 5
         * eachHour : 1
         * releaseTitle : 明年
         * canRent : 1
         * tags : 车位
         * uid : 1869342351
         * province : 广东省
         * geotable_id : 158209
         * district : 宝安区
         * modify_time : 1480084907
         * create_time : 1480003763
         * firstStopPrice : 10
         * parkingLotId : 1869342342
         * rentalDate : 星期一
         * city : 深圳市
         * location : [113.853605,22.606691]
         * address : 地下室
         * title : 固戍-地铁站
         * coord_type : 3
         * type : 0
         * distance : 0
         * weight : 0
         */

        private String firstHour;
        private String startTime;
        private String parkingNumber;
        private String remark;
        private String contacts;
        private String userAccount;
        private String phone;
        private String state;
        private String endTime;
        private String eachCharge;
        private String eachHour;
        private String releaseTitle;
        private String canRent;
        private String tags;
        private long uid;
        private String province;
        private int geotable_id;
        private String district;
        private int modify_time;
        private int create_time;
        private String firstStopPrice;
        private String parkingLotId;
        private String rentalDate;
        private String city;
        private String address;
        private String title;
        private int coord_type;
        private int type;
        private int distance;
        private int weight;
        private List<Double> location;

        public String getFirstHour() {
            return firstHour;
        }

        public void setFirstHour(String firstHour) {
            this.firstHour = firstHour;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getParkingNumber() {
            return parkingNumber;
        }

        public void setParkingNumber(String parkingNumber) {
            this.parkingNumber = parkingNumber;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getUserAccount() {
            return userAccount;
        }

        public void setUserAccount(String userAccount) {
            this.userAccount = userAccount;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getEachCharge() {
            return eachCharge;
        }

        public void setEachCharge(String eachCharge) {
            this.eachCharge = eachCharge;
        }

        public String getEachHour() {
            return eachHour;
        }

        public void setEachHour(String eachHour) {
            this.eachHour = eachHour;
        }

        public String getReleaseTitle() {
            return releaseTitle;
        }

        public void setReleaseTitle(String releaseTitle) {
            this.releaseTitle = releaseTitle;
        }

        public String getCanRent() {
            return canRent;
        }

        public void setCanRent(String canRent) {
            this.canRent = canRent;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public int getGeotable_id() {
            return geotable_id;
        }

        public void setGeotable_id(int geotable_id) {
            this.geotable_id = geotable_id;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public int getModify_time() {
            return modify_time;
        }

        public void setModify_time(int modify_time) {
            this.modify_time = modify_time;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public String getFirstStopPrice() {
            return firstStopPrice;
        }

        public void setFirstStopPrice(String firstStopPrice) {
            this.firstStopPrice = firstStopPrice;
        }

        public String getParkingLotId() {
            return parkingLotId;
        }

        public void setParkingLotId(String parkingLotId) {
            this.parkingLotId = parkingLotId;
        }

        public String getRentalDate() {
            return rentalDate;
        }

        public void setRentalDate(String rentalDate) {
            this.rentalDate = rentalDate;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCoord_type() {
            return coord_type;
        }

        public void setCoord_type(int coord_type) {
            this.coord_type = coord_type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public List<Double> getLocation() {
            return location;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }
    }
}
