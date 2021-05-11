FROM ruby
RUN gem install webrick
COPY . /mnt
WORKDIR /mnt
CMD ruby -run -ehttpd . -p8080