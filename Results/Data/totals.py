__author__ = "lwelna"


def countTotals(cvsFile):  # this function is redundant
    ths = 1  # total heap size
    pop = 0
    push = 0
    remove = 0
    numLines = 0
    for line in cvsFile:
        if "push" in line:
            ths = ths + 1
            push = push + 1
            numLines = numLines + 1
        elif "pop" in line:
            ths = ths - 1
            pop = pop + 1
            numLines = numLines + 1
        elif "remove" in line:
            ths = ths - 1
            remove = remove + 1
            numLines = numLines + 1
    return (push, pop, remove, numLines)


def export100(fileName, cvsFile):
    print("starting export100")
    mod = int(len(cvsFile) / 100 + .5)
    lineNum = 0
    curHeapSize = 1
    for line in cvsFile:
        if "push" in line:
            curHeapSize = curHeapSize + 1
            lineNum = lineNum + 1
        elif "pop" in line:
            curHeapSize = curHeapSize - 1
            lineNum = lineNum + 1
        elif "remove" in line:
            curHeapSize = curHeapSize - 1
            lineNum = lineNum + 1
        elif "new path" in line and "astr" in fileName:
            curHeapSize = 1

        if lineNum % mod == 0:
            exportData = str(lineNum) + "," + str(curHeapSize) + "\n"
            exportFile = open("manageableData/" + fileName + ".csv", "a")
            # print("trying to append to file manageableData/" + fileName + ".csv")
            exportFile.write(exportData)

    print("done with export100")

astr150 = open("Data/astr150.csv").read().split("\n")
dstrLite150 = open("Data/dstrLite150.csv").read().split("\n")

astr200 = open("Data/astr200.csv").read().split("\n")
dstrLite200 = open("Data/dstrLite200.csv").read().split("\n")

astr250 = open("Data/astr250.csv").read().split("\n")
dstrLite250 = open("Data/dstrLite250.csv").read().split("\n")

astr500 = open("Data/astr500.csv").read().split("\n")
dstrLite500 = open("Data/dstrLite500.csv").read().split("\n")

astr700 = open("Data/astr700.csv").read().split("\n")
dstrLite700 = open("Data/dstrLite700.csv").read().split("\n")

astr1000 = open("Data/astr1000.csv").read().split("\n")
dstrLite1000 = open("Data/dstrLite1000.csv").read().split("\n")

print(countTotals(astr1000))
print(countTotals(dstrLite1000))

export100("astr150", astr150)
export100("dstrLite150", dstrLite150)

export100("astr200", astr200)
export100("dstrLite200", dstrLite200)

export100("astr250", astr250)
export100("dstrLite250", dstrLite250)

export100("astr500", astr500)
export100("dstrLite500", dstrLite500)

export100("astr700", astr700)
export100("dstrLite700", dstrLite700)

export100("astr1000", astr1000)
export100("dstrLite1000", dstrLite1000)
