package server;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

public class UserSet extends HashSet<User>
{
    UserSet()
    {
        super();
    }

    synchronized public boolean isUserOnline(String username)
    {
        Iterator<User> it = this.iterator();
        while(it.hasNext())
        {
            if(it.next().username.equals(username))
                return true;
        }
        return false;
    }

    public User getUser(String username)
    {
        Iterator<User> it = this.iterator();
        while(it.hasNext())
        {
            User o = it.next();
            if(o.username.equals(username))
               return o;
        }
        return null;
    }

    synchronized  public void removeUser(String username)
    {
        Iterator<User> it = this.iterator();
        while(it.hasNext())
        {
            User o = it.next();
            if(o.username.equals(username)) {
                this.remove(o);
                break;
            }
        }
    }

    synchronized public boolean addUser(User r)
    {
        return this.add(r);
    }

}
