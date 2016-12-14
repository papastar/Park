package com.papa.park.entity.bean;

import java.util.List;

/**
 * User: PAPA
 * Date: 2016-12-01
 */

public class LockerLBSListResponse {


    /**
     * status : 0
     * size : 3
     * total : 3
     * pois : [{"address":"风和日丽","city":"深圳市","create_time":"2016-12-07 21:27:59",
     * "district":"宝安区","geotable_id":158209,"location":[114.035878,22.644626],
     * "lockerBlueAddress":"88:0F:10:A6:08:C4","lockerBlueName":"MI_SCALE",
     * "lockerId":"57fa4991eaa2a2641b62ec23","lockerKey":"IOwBpJmtBMQqGhdt",
     * "lockerParkName":"风和日丽停车场","lockerSn":"3f20d52036",
     * "lockerToken":"c67d47a60b6f69c0cf119aeb5e7ddaa4","modify_time":"2016-12-08 11:13:58",
     * "ownerId":"57ee0c9c7ad35e4b7673d6da","ownerName":"爬爬","ownerPhone":"13418459758",
     * "province":"广东省","rentState":0,"tags":"车位","title":"风和日丽车位","gcj_location":[114
     * .02939645144,22.638618085],"city_id":340,"id":1883322571},{"address":"东方科技大厦",
     * "city":"深圳市","create_time":"2016-12-07 21:27:59","district":"宝安区","geotable_id":158209,
     * "location":[113.853503,22.606228],"lockerParkName":"地下停车场","modify_time":"2016-12-08
     * 15:56:22","ownerName":"爬爬","ownerPhone":"13418459758","province":"广东省",
     * "rentFirstHourPrice":"10","rentPerHourPrice":"5","rentStartTime":"10","rentState":2,
     * "tags":"车位","title":"固戍地铁站-f口","gcj_location":[113.84697553344,22.600278026779],
     * "city_id":340,"id":1883322560},{"address":"雪松大厦","city":"广州市","create_time":"2016-12-07
     * 21:27:59","district":"从化市","geotable_id":158209,"location":[113.663503,23.606228],
     * "lockerBlueAddress":"88:0F:10:A6:08:C4","lockerBlueName":"MI_SCALE",
     * "lockerId":"57fa4991eaa2a2641b62ec23","lockerKey":"IOwBpJmtBMQqGhdt",
     * "lockerParkName":"雪松大厦停车场","lockerSn":"3f20d52036",
     * "lockerToken":"c67d47a60b6f69c0cf119aeb5e7ddaa4","modify_time":"2016-12-08 16:03:45",
     * "ownerId":"57ee0c9c7ad35e4b7673d6da","ownerName":"爬爬","ownerPhone":"13418459758",
     * "province":"广东省","rentEndTime":"1481199947000","rentFirstHourPrice":"20",
     * "rentPerHourPrice":"15","rentStartTime":"1481183947000","rentState":3,"tags":"车位",
     * "tenantId":"57ee0c9c7ad35e4b7673d6da","tenantName":"移动","tenantPhone":"13800138000",
     * "title":"车公庙雪松大厦","gcj_location":[113.65706462193,23.600017006905],"city_id":257,
     * "id":1883322558}]
     * message : 成功
     */

    private int status;
    private int size;
    private int total;
    private String message;
    private List<PoisBean> pois;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PoisBean> getPois() {
        return pois;
    }

    public void setPois(List<PoisBean> pois) {
        this.pois = pois;
    }

    public static class PoisBean {
        /**
         * address : 风和日丽
         * city : 深圳市
         * create_time : 2016-12-07 21:27:59
         * district : 宝安区
         * geotable_id : 158209
         * location : [114.035878,22.644626]
         * lockerBlueAddress : 88:0F:10:A6:08:C4
         * lockerBlueName : MI_SCALE
         * lockerId : 57fa4991eaa2a2641b62ec23
         * lockerKey : IOwBpJmtBMQqGhdt
         * lockerParkName : 风和日丽停车场
         * lockerSn : 3f20d52036
         * lockerToken : c67d47a60b6f69c0cf119aeb5e7ddaa4
         * modify_time : 2016-12-08 11:13:58
         * ownerId : 57ee0c9c7ad35e4b7673d6da
         * ownerName : 爬爬
         * ownerPhone : 13418459758
         * province : 广东省
         * rentState : 0
         * tags : 车位
         * title : 风和日丽车位
         * gcj_location : [114.02939645144,22.638618085]
         * city_id : 340
         * id : 1883322571
         * rentFirstHourPrice : 10
         * rentPerHourPrice : 5
         * rentStartTime : 10
         * rentEndTime : 1481199947000
         * tenantId : 57ee0c9c7ad35e4b7673d6da
         * tenantName : 移动
         * tenantPhone : 13800138000
         */

        private String address;
        private String city;
        private String create_time;
        private String district;
        private int geotable_id;
        private String lockerBlueAddress;
        private String lockerBlueName;
        private String lockerId;
        private String lockerKey;
        private String lockerParkName;
        private String lockerSn;
        private String lockerToken;
        private String modify_time;
        private String ownerId;
        private String ownerName;
        private String ownerPhone;
        private String province;
        private int rentState;
        private String tags;
        private String title;
        private int city_id;
        private int id;
        private String rentFirstHourPrice;
        private String rentPerHourPrice;
        private String rentStartTime;
        private String rentEndTime;
        private String tenantId;
        private String tenantName;
        private String tenantPhone;
        private String reserveStartTime;
        private List<Double> location;
        private List<Double> gcj_location;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public int getGeotable_id() {
            return geotable_id;
        }

        public void setGeotable_id(int geotable_id) {
            this.geotable_id = geotable_id;
        }

        public String getLockerBlueAddress() {
            return lockerBlueAddress;
        }

        public void setLockerBlueAddress(String lockerBlueAddress) {
            this.lockerBlueAddress = lockerBlueAddress;
        }

        public String getLockerBlueName() {
            return lockerBlueName;
        }

        public void setLockerBlueName(String lockerBlueName) {
            this.lockerBlueName = lockerBlueName;
        }

        public String getLockerId() {
            return lockerId;
        }

        public void setLockerId(String lockerId) {
            this.lockerId = lockerId;
        }

        public String getLockerKey() {
            return lockerKey;
        }

        public void setLockerKey(String lockerKey) {
            this.lockerKey = lockerKey;
        }

        public String getLockerParkName() {
            return lockerParkName;
        }

        public void setLockerParkName(String lockerParkName) {
            this.lockerParkName = lockerParkName;
        }

        public String getLockerSn() {
            return lockerSn;
        }

        public void setLockerSn(String lockerSn) {
            this.lockerSn = lockerSn;
        }

        public String getLockerToken() {
            return lockerToken;
        }

        public void setLockerToken(String lockerToken) {
            this.lockerToken = lockerToken;
        }

        public String getModify_time() {
            return modify_time;
        }

        public void setModify_time(String modify_time) {
            this.modify_time = modify_time;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getOwnerPhone() {
            return ownerPhone;
        }

        public void setOwnerPhone(String ownerPhone) {
            this.ownerPhone = ownerPhone;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public int getRentState() {
            return rentState;
        }

        public void setRentState(int rentState) {
            this.rentState = rentState;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRentFirstHourPrice() {
            return rentFirstHourPrice;
        }

        public void setRentFirstHourPrice(String rentFirstHourPrice) {
            this.rentFirstHourPrice = rentFirstHourPrice;
        }

        public String getRentPerHourPrice() {
            return rentPerHourPrice;
        }

        public void setRentPerHourPrice(String rentPerHourPrice) {
            this.rentPerHourPrice = rentPerHourPrice;
        }

        public String getRentStartTime() {
            return rentStartTime;
        }

        public void setRentStartTime(String rentStartTime) {
            this.rentStartTime = rentStartTime;
        }

        public String getRentEndTime() {
            return rentEndTime;
        }

        public void setRentEndTime(String rentEndTime) {
            this.rentEndTime = rentEndTime;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }

        public String getTenantPhone() {
            return tenantPhone;
        }

        public void setTenantPhone(String tenantPhone) {
            this.tenantPhone = tenantPhone;
        }

        public List<Double> getLocation() {
            return location;
        }

        public void setLocation(List<Double> location) {
            this.location = location;
        }

        public List<Double> getGcj_location() {
            return gcj_location;
        }

        public void setGcj_location(List<Double> gcj_location) {
            this.gcj_location = gcj_location;
        }

        public String getReserveStartTime() {
            return reserveStartTime;
        }

        public void setReserveStartTime(String reserveStartTime) {
            this.reserveStartTime = reserveStartTime;
        }

        public String getLockerAddress() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(province).append(city).append(district).append(address);
            return buffer.toString();
        }
    }
}
