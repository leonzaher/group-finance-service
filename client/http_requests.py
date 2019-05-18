import requests

### CHANGE THIS AS NEEDED
SERVER_URL = "http://localhost:8080"

### DO NOT CHANGE ANYTHING BELOW THIS
group_endpoint = "/group"
user_endpoint = "/user"
balance_endpoint = "/balance"
group_payment_endpoint = "/group"
detailed_payment_endpoint = "/detailed"


def group_create(args):
    r = requests.post(SERVER_URL + group_endpoint, json={'name': args.name})
    print(r.status_code, r.reason)
    print(r.text)

def group_list(args):
    r = requests.get(SERVER_URL + group_endpoint)
    print(r.status_code, r.reason)
    print(r.text)

def group_get(args):
    r = requests.get(SERVER_URL + group_endpoint, data={'name': args.name})
    print(r.status_code, r.reason)
    print(r.text)

def group_update(args):
    r = requests.put(SERVER_URL + group_endpoint, json={'name': args.name})
    print(r.status_code, r.reason)
    print(r.text)

def group_delete(args):
    r = requests.delete(SERVER_URL + group_endpoint, data={'name': args.name})
    print(r.status_code, r.reason)
    print(r.status_code, r.reason)


def user_create(args):
    r = requests.post(SERVER_URL + user_endpoint, data={'group': args.group}, json={'username': args.username})
    print(r.status_code, r.reason)
    print(r.text)

def user_list(args):
    r = requests.get(SERVER_URL + user_endpoint, data={'group': args.group})
    print(r.status_code, r.reason)
    print(r.text)

def user_update(args):
    r = requests.put(SERVER_URL + user_endpoint, data={'group': args.group, 'username': args.username}, json={'username': args.updated_username})
    print(r.status_code, r.reason)
    print(r.text)

def user_delete(args):
    r = requests.delete(SERVER_URL + user_endpoint, data={'group': args.group, 'username': args.username})
    print(r.status_code, r.reason)
    print(r.status_code, r.reason)


def balance_increase(args):
    r = requests.put(SERVER_URL + balance_endpoint, data={'group': args.group, 'username': args.username, 'amount': args.amount})
    print(r.status_code, r.reason)
    print(r.text)

def balance_decrease(args):
    r = requests.put(SERVER_URL + balance_endpoint, data={'group': args.group, 'username': args.username, 'amount': args.amount})
    print(r.status_code, r.reason)
    print(r.text)

def balance_payment(args):
    r = requests.put(SERVER_URL + balance_endpoint, data={'group': args.group, 'payer': args.payer, 'payee': args.payee, 'amount': args.amount})
    print(r.status_code, r.reason)
    print(r.text)

def balance_group_payment(args):
    r = requests.put(SERVER_URL + balance_endpoint + group_payment_endpoint, data={'group': args.group, 'payer': args.payer, 'amount': args.amount})
    print(r.status_code, r.reason)
    print(r.text)

def balance_detailed_payment(args):
    r = requests.put(SERVER_URL + balance_endpoint + detailed_payment_endpoint, data={'group': args.group, 'payer': args.payer, 'paymentDetails': args.payment_details})
    print(r.status_code, r.reason)
    print(r.text)