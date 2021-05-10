#!/bin/bash
set -eo pipefail

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd $DIR

rm -rf package
cd function
pip install --target ../package/python -r requirements.txt