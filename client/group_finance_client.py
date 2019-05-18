from argument_parser import parse_args, ArgumentParserError


def main():
    print("Welcome to Group Finance. \n"
          "This client is continuously listening for commands. Input a command and press Enter.\n"
          "If you do not know the commands, check out README.md")

    while True:
        line = input()
        if line == 'exit':
            print("bye")
            break

        try:
            parse_args(line.split(" "))
        except ArgumentParserError as err:
            print(err)

        print("")


if __name__ == '__main__':
    main()
