package nl.hsleiden.ipsene.observers;

import nl.hsleiden.ipsene.models.Account;

public interface AccountObservable {
    void update(Account account);
}
