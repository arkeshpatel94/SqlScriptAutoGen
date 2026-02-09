if [ $1 != "CURRENT_PATH" ]
then
 cd $1
fi

sed -e "s/$2/\"/g" -e "s/PIPESEP/|/g" $3 >$4

rm -f $3
