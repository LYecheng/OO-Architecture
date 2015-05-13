cat $1 | gawk '{
system("sleep 1");
filename = strftime("%d-%m-%y_%H-%M-%S",systime())".csv"
print filename
print $0 > filename
}'

