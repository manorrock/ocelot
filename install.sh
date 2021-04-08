#!/bin/bash
set -u

abort() {
    printf "%s\n" "$@"
    exit 1
}

if [ -z "${BASH_VERSION:-}" ]; then
  abort "Bash is required to run this script."
fi

OS="$(uname)"

if [[ "$OS" == "Darwin" ]]; then
  OCELOT_ON_MACOS=1
else
  abort "Installer currently only supports macOS"
fi

if [[ -z "${OCELOT_ON_MACOS-}" ]]; then
  OCELOT_PREFIX="/usr/local/ocelot"
fi

sudo mkdir $OCELOT_PREFIX || true
cd $OCELOT_PREFIX
sudo curl --output ocelot.tar.gz -O https://oss.sonatype.org/service/local/repositories/snapshots/content/com/manorrock/ocelot/ocelot-cli-macos/21.5.0-SNAPSHOT/ocelot-cli-macos-21.5.0-20210407.194244-2.tar.gz
sudo tar xfz ocelot.tar.gz
sudo rm ocelot.tar.gz

sudo ln -s $OCELOT_PREFIX/bin/mo /usr/local/bin/mo

echo "Installed Manorrock Ocelot into $OCELOT_PREFIX"
echo "Do not forget to add $OCELOT_PREFIX/bin to your PATH"
