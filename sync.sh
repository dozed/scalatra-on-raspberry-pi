#!/bin/bash

HOST=raspberrypi
DEST=1/classes

if [ -z "$DEST" ]; then exit 1; fi

inotifywait -r -m -e close_write --format '%w%f' . |\
while read file
do
  echo $file
  # echo "scp $file ${DEST}/$file"
  scp $file "${HOST}:'${DEST}/${file}'"
  # echo -n 'Completed at '
  # date
done
