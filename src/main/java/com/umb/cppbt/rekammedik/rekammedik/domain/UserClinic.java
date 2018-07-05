package com.umb.cppbt.rekammedik.rekammedik.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Table(name = "user_clinic")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserClinic implements UserDetails, Serializable {
    
	private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    
    @Column(name = "date_birth")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateBirth;
    
    @Size(max = 70)
    @Column(name = "place_birth")
    private String placeBirth;
    
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    
    @Size(max = 255)
    @Column(name = "full_name")
    private String fullName;
    
    @Size(max = 255)
    @Column(name = "password")
    private String password;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Size(max = 255)
    @Column(name = "photo_path")
    private String photoPath;
    
    @Size(max = 255)
    @Column(name = "username")
    private String username;
    
    @Column(name = "first_registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstRegistrationDate;
   
    @Column(name = "latitude")
    private Float latitude;
    
    @Column(name = "longitude")
    private Float longitude;
    
    @Size(max = 10)
    @Column(name = "user_code")
    private String userCode;
       
    @Size(max = 50)
    @Column(name = "gender")
    private String gender ;
    
    @Size(max = 50)
    @Column(name = "religion")
    private String religion;
    
    @JoinColumn(name = "id_userclinic_status", referencedColumnName = "id")
    @ManyToOne
    private Status status;
    
    @JoinColumn(name = "id_clinic", referencedColumnName = "id")
    @ManyToOne
    private Clinic clinic;
    
    @ElementCollection
    //@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<String> roles = new ArrayList<>();

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public UserClinic() {
    }

    public UserClinic(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Clinic getClinic() {
		return clinic;
	}

	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	@JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles)
        {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getUsername() {
        return username;
    }
    
    public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getFirstRegistrationDate() {
        return firstRegistrationDate;
    }

    public void setFirstRegistrationDate(Date firstRegistrationDate) {
        this.firstRegistrationDate = firstRegistrationDate;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
    
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
    public String getPlaceBirth() {
		return placeBirth;
	}

	public void setPlaceBirth(String placeBirth) {
		this.placeBirth = placeBirth;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserClinic)) {
            return false;
        }
        UserClinic other = (UserClinic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", address=" + address + ", dateBirth="
				+ dateBirth + ", email=" + email + ", fullName=" + fullName
				+ ", password=" + password + ", phoneNumber=" + phoneNumber
				+ ", photoPath=" + photoPath + ", username=" + username
				+ ", firstRegistrationDate=" + firstRegistrationDate
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", userCode=" + userCode + ", gender=" + gender
				+ ", religion=" + religion + ", roles=" + roles + "]";
	}
    
}
