mkdir -p package/macosx
cp ShowTime.icns package/macosx
jdk=$(/usr/libexec/java_home)
$jdk/bin/javapackager -version
$jdk/bin/javapackager -deploy -native dmg \
   -srcfiles ShowTime.jar -appclass ShowTime -name ShowTime \
   -outdir deploy -outfile ShowTime -v
cp deploy/bundles/ShowTime-1.0.dmg show-time-installer.dmg
ls -l