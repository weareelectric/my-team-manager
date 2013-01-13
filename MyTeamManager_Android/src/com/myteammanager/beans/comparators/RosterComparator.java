package com.myteammanager.beans.comparators;

import java.util.Comparator;

import com.myteammanager.beans.PlayerBean;

public class RosterComparator implements Comparator<PlayerBean> {

	@Override
	public int compare(PlayerBean lhs, PlayerBean rhs) {
		
		int roleDiff = lhs.getRole() - rhs.getRole();
		if ( roleDiff != 0 ) {
			return roleDiff;
		}
		else {
			int lastNameDiff = lhs.getLastName().compareTo(rhs.getLastName());
			if (lastNameDiff !=0) {
				return lastNameDiff; 
			}
			else {
				String name1 = lhs.getName();
				String name2 = rhs.getName();
				
				if ( name1 == null && name2 == null ) {
					return -1;
				}
				else if ( name1 == null ) {
					return -1;
				}
				else if ( name2 == null ) {
					return 1;
				}
				
				return name1.compareTo(name2);
			}
		}
	}

}
