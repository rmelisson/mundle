cd eclipse/ && zip -r eclipse.jar ./META-INF/ ./lib/ && cd ..
cd fa/ && zip -r fa.jar ./META-INF/ ./lib/ && cd ..
cd fact/ && zip -r fact.jar ./META-INF/ ./lib/ && cd ..
cd fcftoo/ && zip -r fcftoo.jar ./META-INF/ ./lib/ && cd ..
cd juliacr/ && zip -r juliacr.jar ./META-INF/ ./lib/ && cd ..
cp /home/remi/.m2/repository/org/ow2/frascati/frascati-assembly-factory/1.4-SNAPSHOT/frascati-assembly-factory-1.4-SNAPSHOT.jar frascatia/lib
cd frascatia/ && zip -r frascatia.jar ./META-INF/ ./lib/ && cd ..
