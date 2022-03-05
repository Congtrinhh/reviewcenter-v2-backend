package com.trinhquycong.restloginreviewcenter.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateInfoBoxDto {
	
	/**
	 * rateInfo:
	 * [
	 * 	{
	 * 	"rateNumber":1,
	 * 	"totalRatedTimes: 98,
	 * 	},
	 * ]
	 */
	private List<DetailedRate> rateInfo;
	
	private Long totalRateTimes;
	
	private Float averagePercentRate; // 0 -> 100 (tính phần trăm)
	
	public void calcAveragePercentRate() {
		if (rateInfo==null) {
			return;
		}
		if (totalRateTimes==null||totalRateTimes==0) {
			return;
		}
		if (rateInfo.size()!=5) { // 1, 2, 3, 4, 5
			return;
		}
		Long tuSo = 0L;
		for (DetailedRate rate : rateInfo) {
			tuSo += rate.getRateNumber() * rate.getTotalRatedTimes();
		}
		Long mauSo = 5 * totalRateTimes;
		averagePercentRate = (float) ((tuSo*100*1.0)/mauSo);
	}

	public RateInfoBoxDto(List<DetailedRate> rateInfo, Long totalRateTimes) {
		super();
		this.rateInfo = rateInfo;
		this.totalRateTimes = totalRateTimes;
	}

}
