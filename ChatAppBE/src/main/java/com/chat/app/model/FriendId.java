package com.chat.app.modal;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FriendId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer user;
	
    private Integer friend;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendId friendId = (FriendId) o;
        return Objects.equals(user, friendId.user) &&
               Objects.equals(friend, friendId.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend);
    }
}
