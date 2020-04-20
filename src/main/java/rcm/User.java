package rcm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity  
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "LogisticsCompanyID",
                       discriminatorType = DiscriminatorType.STRING,
                       length = 20)
@DiscriminatorValue("U")


public abstract class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column
    protected int id;
    @Column
    protected String password;
    @Column
    protected String name;
    @Column
    protected String address;
    @Column
    protected String refPerson;
    @Column
    protected String email;

    private static final String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String regexName = "^[A-Z]+([a-z]*)+(([',. -][a-zA-Z ])?[a-zA-Z]*)*.{2,25}$";
    private static final String regexPassword = "^(?=.*[a-z])(?=.*[0-9])(?=.*[@#$%])*(?=.*[A-Z]).{6,16}$";
    
    
    protected User() {
        
    }
    public User(String name, String address, String refPerson, String email, String password)
            throws WrongInputException {
    
   
        this.address = address;

        if (validateSomeName(name)) {
            this.name = name;
        } else {
            throw new WrongInputException("The given name is not valid");
        }

        if (validateSomeName(refPerson)) {
            this.refPerson = refPerson;
        } else {
            throw new WrongInputException("The given name is not valid");
        }

        if (validateEmail(email)) {
            this.email = email;
        } else {
            throw new WrongInputException("The given email is not valid");
        }

        if (validatePassword(password)) {
            this.password = Password.SHA1_Hasher(password);
        } else {
            throw new WrongInputException("The given password is not valid");
        }
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getRefPerson() {
        return refPerson;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    private static boolean validateSomeName(String someName) {
        Matcher matcherName = Pattern.compile(regexName).matcher(someName);
        Matcher matcherRefPerson = Pattern.compile(regexName).matcher(someName);
        if (matcherName.matches() || matcherRefPerson.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean validateEmail(String email) {
        Matcher matcherEmail = Pattern.compile(regexEmail).matcher(email);
        if (matcherEmail.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean validatePassword(String password) {
        Matcher matcherPassword = Pattern.compile(regexPassword).matcher(password);
        if (matcherPassword.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public void updateName(String newName) {
        if (validateSomeName(newName)) {
            name = newName;
        }
    }

    public void updateAddress(String newAddress) {
        address = newAddress;
    }

    public void updateRefPerson(String newRefPerson) {
        if (validateSomeName(newRefPerson)) {
            refPerson = newRefPerson;
        }
    }

    public void updateEmail(String newEmail) {
        if (validateEmail(newEmail)) {
            email = newEmail;
        }
    }

    public void updatePassword(String newPassword) {
        if (validatePassword(newPassword)) {
            password = Password.SHA1_Hasher(newPassword);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
