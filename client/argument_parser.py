import argparse
import http_requests as req


class PaymentDetails():
    def __init__(self, username, amount):
        self.username = username
        self.amount = amount

    def __repr__(self):
        return 'PaymentDetails(%r, %r)' % (self.username, self.amount)


class PaymentDetailsAction(argparse.Action):
    def __init__(self, *args, **kwargs):
        super(PaymentDetailsAction, self).__init__(*args, **kwargs)
        self.nargs = 2

    def __call__(self, parser, namespace, values, option_string):
        lst = getattr(namespace, self.dest, []) or []
        a, b = values
        lst.append(PaymentDetails(str(a), float(b)))
        setattr(namespace, self.dest, lst)


class ArgumentParserError(Exception):
    pass


class ThrowingArgumentParser(argparse.ArgumentParser):
    def error(self, message):
        raise ArgumentParserError(message)


def parse_args(args):
    parser = ThrowingArgumentParser(description='A command line client for Group Finance service.')
    subparsers = parser.add_subparsers()

    # group commands
    group_p = subparsers.add_parser('group')
    group_subp = group_p.add_subparsers()

    create_gp = group_subp.add_parser('create')
    create_gp.set_defaults(func=req.group_create)
    create_gp.add_argument('--name', dest='name', type=str, required=True)

    list_gp = group_subp.add_parser('list')
    list_gp.set_defaults(func=req.group_list)

    get_gp = group_subp.add_parser('get')
    get_gp.set_defaults(func=req.group_get)
    get_gp.add_argument('--name', dest='name', type=str, required=True)

    update_gp = group_subp.add_parser('update')
    update_gp.set_defaults(func=req.group_update)
    update_gp.add_argument('--name', dest='name', type=str, required=True)
    update_gp.add_argument('--updated_name', dest='updated_name', type=str, required=True)

    delete_gp = group_subp.add_parser('delete')
    delete_gp.set_defaults(func=req.group_delete)
    delete_gp.add_argument('--name', dest='name', type=str, required=True)


    # user commands
    user_p = subparsers.add_parser('user')
    user_subp = user_p.add_subparsers()

    create_up = user_subp.add_parser('create')
    create_up.set_defaults(func=req.user_create)
    create_up.add_argument('--group', dest='group', type=str, required=True)
    create_up.add_argument('--username', dest='username', type=str, required=True)

    list_up = user_subp.add_parser('list')
    list_up.set_defaults(func=req.user_list)
    list_up.add_argument('--group', dest='group', type=str, required=True)

    update_up = user_subp.add_parser('update')
    update_up.set_defaults(func=req.user_update)
    update_up.add_argument('--group', dest='group', type=str, required=True)
    update_up.add_argument('--username', dest='username', type=str, required=True)
    update_up.add_argument('--updated_username', dest='updated_username')
    update_up.add_argument('--balance', dest='balance')

    delete_up = user_subp.add_parser('delete')
    delete_up.set_defaults(func=req.user_delete)
    delete_up.add_argument('--group', dest='group', type=str, required=True)
    delete_up.add_argument('--username', dest='username', type=str, required=True)


    # balance commands
    balance_p = subparsers.add_parser('balance')
    balance_subp = balance_p.add_subparsers()

    increase_p = balance_subp.add_parser('increase')
    increase_p.set_defaults(func=req.balance_increase)
    increase_p.add_argument('--group', dest='group', type=str, required=True)
    increase_p.add_argument('--username', dest='username', type=str, required=True)
    increase_p.add_argument('--amount', dest='amount', type=float, required=True)

    decrease_p = balance_subp.add_parser('decrease')
    decrease_p.set_defaults(func=req.balance_decrease)
    decrease_p.add_argument('--group', dest='group', type=str, required=True)
    decrease_p.add_argument('--username', dest='username', type=str, required=True)
    decrease_p.add_argument('--amount', dest='amount', type=float, required=True)

    payment_p = balance_subp.add_parser('payment')
    payment_p.set_defaults(func=req.balance_payment)
    payment_p.add_argument('--group', dest='group', type=str, required=True)
    payment_p.add_argument('--payer', dest='payer', type=str, required=True)
    payment_p.add_argument('--payee', dest='payee', type=str, required=True)
    payment_p.add_argument('--amount', dest='amount', type=float, required=True)

    group_payment_p = balance_subp.add_parser('group_payment')
    group_payment_p.set_defaults(func=req.balance_group_payment)
    group_payment_p.add_argument('--group', dest='group', type=str, required=True)
    group_payment_p.add_argument('--payer', dest='payer', type=str, required=True)
    group_payment_p.add_argument('--amount', dest='amount', type=float, required=True)

    detailed_payment_p = balance_subp.add_parser('detailed_payment')
    detailed_payment_p.set_defaults(func=req.balance_detailed_payment)
    detailed_payment_p.add_argument('--group', dest='group', type=str, required=True)
    detailed_payment_p.add_argument('--payer', dest='payer', type=str, required=True)
    detailed_payment_p.add_argument('--payment_details', action=PaymentDetailsAction)


    parsed_args = parser.parse_args(args)
    parsed_args.func(parsed_args)
