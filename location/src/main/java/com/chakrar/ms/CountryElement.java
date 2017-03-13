package com.chakrar.ms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryElement {

	public CountryElement() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CountryElement [geonameId=" + geonameId + ", countryCode=" + countryCode + ", countryName="
				+ countryName + ", name=" + name + "]";
	}

	/**
	 * @return the geonameId
	 */
	public String getGeonameId() {
		return geonameId;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((geonameId == null) ? 0 : geonameId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CountryElement other = (CountryElement) obj;
		if (geonameId == null) {
			if (other.geonameId != null)
				return false;
		} else if (!geonameId.equals(other.geonameId))
			return false;
		return true;
	}

	/**
	 * @param geonameId
	 *            the geonameId to set
	 */
	public void setGeonameId(String geonameId) {
		this.geonameId = geonameId;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param countryCode
	 *            the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * @param countryName
	 *            the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	private String geonameId;

	private String countryCode;

	private String countryName;

	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
