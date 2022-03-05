package com.trinhquycong.restloginreviewcenter.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.github.slugify.Slugify;
import com.trinhquycong.restloginreviewcenter.dto.LocalUser;
import com.trinhquycong.restloginreviewcenter.dto.RateNumber;
import com.trinhquycong.restloginreviewcenter.dto.SocialProvider;
import com.trinhquycong.restloginreviewcenter.dto.UserInfo;
import com.trinhquycong.restloginreviewcenter.model.Rating;
import com.trinhquycong.restloginreviewcenter.model.Role;
import com.trinhquycong.restloginreviewcenter.model.User;

public class GeneralUtils {
	public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<Role> roles) {
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
		roles.forEach(role->grantedAuthorities.add(new SimpleGrantedAuthority(role.getName())));
		return grantedAuthorities;
    }
	
	public static SocialProvider toSocialProvider(String providerId) {
        for (SocialProvider socialProvider : SocialProvider.values()) {
        	if (socialProvider.getProviderType().equals(providerId)) {
        		return socialProvider;
        	}
        }
        return SocialProvider.LOCAL;
    }
	
	public static UserInfo buildUserInfo(LocalUser localUser) {
        List<String> roles = localUser.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        User user = localUser.getUser();
        return new UserInfo(user.getId().toString(), user.getDisplayName(), user.getEmail(), user.getAvatarUrl(),  roles);
    }
	
	public static RateNumber getRateNumber(Integer value) {
		RateNumber rateEnum;
		switch (value) {
		case 1:
			rateEnum = RateNumber.ONE;
			break;
		case 2:
			rateEnum = RateNumber.TWO;
			break;
		case 3:
			rateEnum = RateNumber.THREE;
			break;
		case 4:
			rateEnum = RateNumber.FOUR;
			break;
		case 5:
			rateEnum = RateNumber.FIVE;
			break;

		default:
			throw new RuntimeException("value for rate enum not in range (1 to 5)");
		}
		return rateEnum;
	}
	
	public static String createSlug(String text) {
		if (text==null || text.length()==0) {
			return null;
		}
		return new Slugify().slugify(text);
	}
	
	public static boolean containsRole(Collection<Role> roles, String roleName) {
		for (Role r : roles ) {
			if (r.getName().equalsIgnoreCase(roleName)) {
				return true;
			}
		}
		return false;
	}
	
	public static Float calcAverageRateNumber(Collection<Rating> ratings) {
		int countTotal = ratings.size();
		if (countTotal==0) {
			return Float.valueOf(0);
		}
		
		int count1s = 0,count2s=0,count3s=0, count4s=0, count5s=0;
		for (Rating r : ratings) {
			switch (r.getRate()) {
			case ONE:
				count1s++;
				break;
			case TWO:
				count2s++;
				break;
			case THREE:
				count3s++;
				break;
			case FOUR:
				count4s++;
				break;
			case FIVE:
				count5s++;
				break;
			default:
				break;
			}
		}
		return ((count1s*1 + count2s*2 + count3s*3 + count4s*4 + count5s*5)*5F)
				/
				(countTotal*5);
	}
}
