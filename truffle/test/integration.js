const Crowdfunding = artifacts.require("Crowdfunding");
const TWLV = artifacts.require("TWLV");
const Faucet = artifacts.require("twlvFaucet");
const Factory = artifacts.require("CrowdfundingFactory");

let twlv;
let faucet;
let factory;
let crowdfunding;
let accountOne;
let accountTwo;
let accountThree;
let accountFour;
let accountFive;
let accountTen;
let crowdfundingAddress;
let faucetOwner;
let factoryOwner;
const crowdfundingGoal = 500;
const decimals = 1000000;
const requestValue = 300;

contract("Crowdfunding", (accounts) => {
  accountOne = accounts[0];
  accountTwo = accounts[1];
  accountThree = accounts[2];
  accountFour = accounts[3];
  accountFive = accounts[4];

  it("check manager and token owners are the same", async () => {
    factory = await Factory.deployed();
    twlv = await TWLV.deployed();
    faucet = await Faucet.deployed();

    const twlvOwner = await twlv.owner();
    const factoryOwner = await factory.owner();
    assert.equal(
      factoryOwner,
      twlvOwner,
      "Token owner and manager owner is not the same."
    );
  });
  it("check token and faucet owners are the same", async () => {
    const twlvOwner = await twlv.owner();
    faucetOwner = await faucet.owner();
    assert.equal(
      twlvOwner,
      faucetOwner,
      "token and crowdfunding owners are not the same"
    );
  });

  it("check crowdfunding and faucet owners are NOT the same", async () => {
    accountTen = accounts[9];
    await factory.allowAddress(accountTen);
    let CFresult = await factory.createNewProject(
      crowdfundingGoal,
      1000,
      twlv.address,
      "ok la",
      {
        from: accountTen,
      }
    );
    // retrieved from the event emitted
    crowdfundingAddress = CFresult.logs[2].args[0];
    crowdfunding = await Crowdfunding.at(crowdfundingAddress);

    faucetOwner = await faucet.owner();
    crowdfundingOwner = await crowdfunding.owner();
    assert.notEqual(
      faucetOwner,
      crowdfundingOwner,
      "faucet and crowdfunding owners cannot be not the same"
    );
  });

  it("check faucet and factory address are same", async () => {
    factoryOwner = await factory.owner();
    assert.equal(
      factoryOwner,
      faucetOwner,
      "faucet and crowdfunding owners are not the same"
    );
  });

  it("check faucet balance", async () => {
    await twlv.mint(faucet.address, 1000000);
    let balance = await twlv.balanceOf(faucet.address);
    web3;
    assert.isAbove(balance.toNumber(), 0, "faucet has no balance");
  });

  it("check faucet tokenAddress and twlv address", async () => {
    const result = await faucet.tokenAddress();
    const address = await twlv.address;

    // assert.isAbove(balance.toNumber(), 0, "faucet has no balance");
    assert.equal(address, result, " not true");
  });

  it("check faucet distribution", async () => {
    await faucet.toggleFaucet();
    await faucet.distribute({ from: accountTwo });
    const bal = await twlv.balanceOf(accountTwo, { from: accountTwo });

    assert.isAbove(bal.toNumber(), 0, "faucet has no balance");
  });

  it("check contribution", async () => {
    await twlv.mint(faucet.address, 1000000);

    let amountToBeContributed = 100;
    await twlv.approve(crowdfundingAddress, amountToBeContributed, {
      from: accountTwo,
    });
    await factory.contributeToProject(
      crowdfundingAddress,
      amountToBeContributed,
      {
        from: accountTwo,
      }
    );
    // await crowdfunding.contribute(amountToBeContributed, { from: accountTwo });

    let contributedAmount = await crowdfunding.contributers(accountTwo);
    let bal = await faucet.balance();

    assert.equal(contributedAmount.toNumber(), 100, "there are contribution");
  });

  it("check total contribution with multiple contributors", async () => {
    // accountTwo has contributed

    await crowdfunding.createRequest("lalaland", accountTen, 300, {
      from: accountTen,
    });

    await faucet.distribute({ from: accountThree });
    await faucet.distribute({ from: accountFour });
    await faucet.distribute({ from: accountFive });

    // if (faucet.mintable() == false) {
    //   faucet.toggleFaucet();
    // }
    let amountToBeContributed3 = 100;
    let amountToBeContributed4 = 200;
    let amountToBeContributed5 = 300;

    await twlv.approve(crowdfundingAddress, amountToBeContributed3, {
      from: accountThree,
    });
    await twlv.approve(crowdfundingAddress, amountToBeContributed4, {
      from: accountFour,
    });
    await twlv.approve(crowdfundingAddress, amountToBeContributed5, {
      from: accountFive,
    });
    await factory.contributeToProject(
      crowdfundingAddress,
      amountToBeContributed3,
      {
        from: accountThree,
      }
    );
    await factory.contributeToProject(
      crowdfundingAddress,
      amountToBeContributed4,
      {
        from: accountFour,
      }
    );
    await factory.contributeToProject(
      crowdfundingAddress,
      amountToBeContributed5,
      {
        from: accountFive,
      }
    );
    await factory.voteRequestForProject(crowdfundingAddress, 0, {
      from: accountTwo,
    });
    await factory.voteRequestForProject(crowdfundingAddress, 0, {
      from: accountThree,
    });
    await factory.voteRequestForProject(crowdfundingAddress, 0, {
      from: accountFour,
    });
    await factory.voteRequestForProject(crowdfundingAddress, 0, {
      from: accountFive,
    });

    let totalContributedAmount = await crowdfunding.raisedAmount();
    // let expectedAmount = (700 * decimals) / crowdfundingGoal;

    assert.equal(
      totalContributedAmount.toNumber(),
      700,
      "total contribution amount is wrong"
    );
  });

  it("check single requests", async () => {
    // accountTen = await accounts[9];

    const owner = await crowdfunding.owner();

    await crowdfunding.createRequest("lalaland", accountTen, requestValue, {
      from: accountTen,
    });
    const request = await crowdfunding.requests(0);
    const description = request.description;
    assert.equal(description, "lalaland", "description is incorrect");
  });

  it("check value of votes", async () => {
    const request = await crowdfunding.requests(0);
    const valueOfVotes = await request.valueOfVotes;
    let expectedAmount = (700 * decimals) / crowdfundingGoal;
    assert.equal(valueOfVotes.toNumber(), expectedAmount, "value of votes");
  });
  it("check crowdfundingOwner and deployer are the same", async () => {
    const factAddress = factory.address;
    const facttAddress = crowdfunding.contractFactory();
    const cfOwner = crowdfunding.owner();
    assert.equal(crowdfundingOwner, accountTen, "address must be the same");
  });
  it("check receiving contribution", async () => {
    await new Promise((resolve) => setTimeout(resolve, 30000));
    await factory.receiveContributionFromProject(crowdfundingAddress, 0, {
      from: accountTen,
    });
    // await crowdfunding.receiveContribution(0, { from: accountTen });
    let bal = await twlv.balanceOf(accountTen);
    assert.equal(bal.toNumber(), requestValue, "contribution amount wrong");
  });
});
