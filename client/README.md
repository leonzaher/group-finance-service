### Group finance service's command line client

#### How to use:
1. If not installed, install Python 3
2. Start the client using `python3 group_finance_client.py`
3. Input commands
4. If you want to exit the client, input `exit`

#### List of possible commands
`group create --name=example`

`group list`

`group get --name=example`

`group update --name=example --updated_name=updated`

`group delete --name=updated`

---
`user create --group=example --username=user1`

`user list --group=example`

`user update --group=example --username=user1 --updated_username=new_user`

`user update --group=example --username=user1 --balance=5.5`

`user update --group=example --username=user1 --updated_username=new_user --balance=5.5`

`user delete --group=example --username=new_user`

---
`balance increase --group=example --username=user1 --amount=5`

`balance decrease --group=example --username=user1 --amount=5`

`balance payment --group=example --payer=user1 --payee=user2 --amount=5`

`balance group_payment --group=example --payer=user1 --amount=5`

`balance detailed_payment --group=example --payer=user1 --payment_details=[{username: user2, amount: 5}, ...]`