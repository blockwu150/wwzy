package com.enation.app.javashop.model.base.context;


/**
 * 地区对象
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/5/2
 */
public class Region {
    /**
     * 城市id
     */
    private Long cityId;
    /**
     * 镇id
     */
    private Long townId;
    /**
     * 县区id
     */
    private Long countyId;
    /**
     * 省id
     */
    private Long provinceId;
    /**
     * 省名称
     */
    private String province;
    /**
     * 县区名称
     */
    private String county;
    /**
     * 城市名称
     */
    private String city;
    /**
     * 镇名称
     */
    private String town;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getTownId() {
        return townId;
    }

    public void setTownId(Long townId) {
        this.townId = townId;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Override
    public String toString() {
        return "Region{" +
                "cityId=" + cityId +
                ", townId=" + townId +
                ", countyId=" + countyId +
                ", provinceId=" + provinceId +
                ", province=" + province +
                ", county=" + county +
                ", city=" + city +
                ", town=" + town +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Region region = (Region) o;

        if (cityId != null ? !cityId.equals(region.cityId) : region.cityId != null) {
            return false;
        }
        if (townId != null ? !townId.equals(region.townId) : region.townId != null) {
            return false;
        }
        if (countyId != null ? !countyId.equals(region.countyId) : region.countyId != null) {
            return false;
        }
        if (provinceId != null ? !provinceId.equals(region.provinceId) : region.provinceId != null) {
            return false;
        }
        if (province != null ? !province.equals(region.province) : region.province != null) {
            return false;
        }
        if (county != null ? !county.equals(region.county) : region.county != null) {
            return false;
        }
        if (city != null ? !city.equals(region.city) : region.city != null) {
            return false;
        }
        return town != null ? town.equals(region.town) : region.town == null;
    }

    @Override
    public int hashCode() {
        int result = cityId != null ? cityId.hashCode() : 0;
        result = 31 * result + (townId != null ? townId.hashCode() : 0);
        result = 31 * result + (countyId != null ? countyId.hashCode() : 0);
        result = 31 * result + (provinceId != null ? provinceId.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (town != null ? town.hashCode() : 0);
        return result;
    }
}
