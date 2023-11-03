# load packages

library(rcompanion)
library(cluster)
library(pracma)

# Selecting Market Segments

set.seed(07054)
hosp = read.csv("/Users/biancoblanco/Downloads/hospitalUSA.csv")
n = dim(hosp)[1]
x = round(runif(1, min=3000, max = 3500), digits = 0)
samphosp = sample(n, x)
hospred = hosp[samphosp,]
hospred$SALES[hospred$SALES == 0] = NA
salescol = hospred$SALES
demohospred = data.frame(hospred[,5:9],hospred[,13:15])
operhospred = data.frame(hospred[,11:12], hospred[,16:18])
summary(hospred)

# plot graphs with non-transformed variables

plotNormalHistogram(hospred$RBEDS)
plotNormalHistogram(hospred$BEDS)
plotNormalHistogram(hospred$OUTV)
plotNormalHistogram(hospred$ADM)
plotNormalHistogram(hospred$SIR)
plotNormalHistogram(hospred$HIP)
plotNormalHistogram(hospred$KNEE)
plotNormalHistogram(hospred$HIP2)
plotNormalHistogram(hospred$KNEE2)
plotNormalHistogram(hospred$FEMUR)

# Transforming variables

logBdemohospred = log(1+.1*demohospred$BEDS)
demohospred$BEDS = logBdemohospred

logOdemohospred = log(1+.01*demohospred$OUTV)
demohospred$OUTV = logOdemohospred

logAdemohospred = log(1 + .01*demohospred$ADM)
demohospred$ADM = logAdemohospred

logSdemohospred = log(1 + .01*demohospred$SIR)
demohospred$SIR = logSdemohospred

logHoperhospred = nthroot(operhospred$HIP, 500)
operhospred$HIP = logHoperhospred

logKoperhospred = nthroot(operhospred$KNEE, 8)
operhospred$KNEE = logKoperhospred

logH2operhospred = nthroot(operhospred$HIP2, 7)
operhospred$HIP2 = logH2operhospred

logK2operhospreed = nthroot(operhospred$KNEE2, 9)
operhospred$KNEE2 = logK2operhospreed

logFoperhospred = nthroot(operhospred$FEMUR, 5)
operhospred$FEMUR = logFoperhospred

# plot graph with transformed variables

plotNormalHistogram(demohospred$RBEDS)
plotNormalHistogram(demohospred$BEDS)
plotNormalHistogram(demohospred$OUTV)
plotNormalHistogram(demohospred$ADM)
plotNormalHistogram(demohospred$SIR)
plotNormalHistogram(operhospred$HIP)
plotNormalHistogram(operhospred$KNEE)
plotNormalHistogram(operhospred$HIP2)
plotNormalHistogram(operhospred$KNEE2)
plotNormalHistogram(operhospred$FEMUR)


# Dimension Reduction

summary(operhospred)
summary(demohospred)
cor(operhospred)
cor(demohospred)

operhospredpca = princomp(operhospred, scores = TRUE, cor = TRUE)
demohospredpca = princomp(demohospred, scores = TRUE, cor = TRUE)

loadings(operhospredpca)
loadings(demohospredpca)

plot(operhospredpca)
plot(demohospredpca)
screeplot(operhospredpca, type = "line", main = "Scree Plot of Operational Variables")
screeplot(demohospredpca, type = "line", main = "Scree Plot of Demographic Variables")

hospredfa = factanal(cbind(demohospred, operhospred), factors = 3); hospredfa
hospredfa2 = factanal(cbind(demohospred, operhospred), factors = 3, rotation = "varimax"); hospredfa2

varimax(hospredfa$loadings)

opdemhospred = cbind(operhospred$HIP2, demohospred$ADM, demohospred$RBEDS)

# Cluster Analysis

silopdemhospred = silhouette(pam(opdemhospred, k = 3))
plot(silopdemhospred, main = paste("k = ", 3), col=2:4, border = NA)

clusterinfo = pam(opdemhospred, 3)
clusterelement = which(clusterinfo$clustering == 1)
salesnewcol = hospred$SALES[clusterelement]
salesnewcolelement = which(is.na(salesnewcol))
redsalesnewcol = hospred[salesnewcolelement,]
opervar = (redsalesnewcol$HIP + redsalesnewcol$KNEE + redsalesnewcol$HIP2 
              + redsalesnewcol$KNEE2 + redsalesnewcol$FEMUR)
meanoper = sum(opervar)/(5*1143)
targetsaleselement = which(opervar >= ceiling(meanoper)) 
targetsalesmat = hospred[targetsaleselement,]
targetsalesmatelem = which(is.na(targetsalesmat$SALES))
targetsalesmatc = targetsalesmat[targetsalesmatelem,]; targetsalesmatc


