package rcm;

public abstract class User {

    protected int id;
    protected String name;
    protected String address;
    protected String refPerson;
    protected String email;
    
    public User(String name, String address, String refPerson, String email) {
        this.name = name;
        this.address = address;
        this.refPerson = refPerson;
        this.email = email;
        id = IdGenerator.getInstance().getId(GroupIdType.USER);
    }

    public int getId() {
        return id;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
