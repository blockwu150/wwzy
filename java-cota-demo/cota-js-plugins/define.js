"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const ckb_sdk_utils_1 = require("@nervosnetwork/ckb-sdk-utils");
const cota_sdk_1 = require("@nervina-labs/cota-sdk");
let address = process.argv[2];
let private_key = process.argv[3];
let name = process.argv[4];
let description = process.argv[5];
let image = process.argv[6];
let num = parseInt(process.argv[7]);
const secp256k1CellDep = (isMainnet) => {
    if (isMainnet) {
        return { outPoint: {
                txHash: "0x71a7ba8fc96349fea0ed3a5c47992e3b4084b031a42264a018e0072e8172e46c",
                index: "0x0",
            }, depType: 'depGroup' };
    }
    return { outPoint: {
            txHash: "0xf8de3bb47d055cdf460d93a2a6e1b05f7432f9777c8c474abf4eec1d4aee5d37",
            index: "0x0",
        }, depType: 'depGroup' };
};
const run = () => __awaiter(void 0, void 0, void 0, function* () {
    // True for mainnet and false for testnet
    const isMainnet = true;
    const service = {
        collector: new cota_sdk_1.Collector({ ckbNodeUrl: 'nodeurl', ckbIndexerUrl: 'ckbIndexerUrl' }),
        aggregator: new cota_sdk_1.Aggregator({ registryUrl: 'registryurl', cotaUrl: 'cotaUrl ' }),
    };
    const ckb = service.collector.getCkb();
    const defineLock = (0, ckb_sdk_utils_1.addressToScript)(address);
    const cotaInfo = {
        name: name,
        description: description,
        image: image,
    };
    let { rawTx, cotaId } = yield (0, cota_sdk_1.generateDefineCotaTx)(service, defineLock, num, '0x00', cotaInfo, cota_sdk_1.FEE, isMainnet);
    // console.log(`cotaId: ${cotaId}`)
    rawTx.cellDeps.push(secp256k1CellDep(isMainnet));
    const signedTx = ckb.signTransaction(private_key)(rawTx);
    // console.log(JSON.stringify(signedTx))
    let txHash = yield ckb.rpc.sendTransaction(signedTx, 'passthrough');
    // console.info(`Define cota nft tx has been sent with tx hash ${txHash}`)
    let ret = { txHash, cotaId, signedTx };
    console.log(ret);
});
run();
