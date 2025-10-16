Process plus gives a decimal using decimal a, decimal b by doing: {

    set decimal total as a + b.

    gives total.

}

Process minus gives a decimal using decimal a, decimal b by doing: {

    set decimal total as a - b.

    gives total.

}

Process multi gives a decimal using decimal a, decimal b by doing: {

    set decimal total as a * b.

    gives total.

}

Process divi gives a decimal using decimal a, decimal b by doing: {

    set decimal total as a / b.

    gives total.

}

set boolean auth as true.

while auth = true do : {

    set decimal a as input from "Enter 1st Number: ".
    set number op as input from "Enter Operator:
1. Add
2. Subtract	
3. Multiply
4. Divide
Choice: ".
    set decimal b as input from "Enter 2nd Number: ".
	set decimal result as 0.
    find op then do: {
        is 1: { set result as input from Using plus with a, b. }
        is 2: { set result as input from Using minus with a, b. }
        is 3: { set result as input from Using multi with a, b. }
        is 4: { set result as input from Using divi with a, b. }
        absent: {print " Invalid ". }
    }
	
	print result.
}