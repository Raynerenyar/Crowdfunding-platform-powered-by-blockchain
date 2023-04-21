//SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.0;

// import "@openzeppelin/contracts/access/AccessControl.sol";

import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract Crowdfunding is Ownable {
    address public admin;
    address public tokenAddress;
    address public crowdfundingFactory;

    IERC20 public token;

    uint public noOfContributors;
    uint public minimumContribution;
    uint public deadline;
    uint public goal;
    uint public raisedAmount;
    uint decimals = 10 ** 6;

    struct Request {
        string description;
        address recipient;
        uint amount;
        bool completed;
        uint noOfVoters;
        uint valueOfVotes;
        mapping(address => bool) voters;
    }
    // cannot store Request in array as it contains mapping
    // therefore use mapping
    uint public numRequests;
    mapping(uint => Request) public requests;
    mapping(address => uint) public contributers;

    // Events
    event ContributeEvent(address _sender, uint _value);
    event ReceiveContributionEvent(address _recipient, uint _value);
    event CreateRequestEvent(
        string _desrciption,
        address _recipient,
        uint _value
    );
    event RefundFromProject(
        address projectAddress,
        address contributer,
        address RefundToken,
        uint RefundAmount
    );

    // allows factory to call the functions here
    modifier sharedOwner() {
        require(
            msg.sender == owner() || msg.sender == crowdfundingFactory,
            "You are neither the factory nor the owner"
        );
        _;
    }

    constructor(
        address _crowdfundingFactory,
        uint _goal,
        uint _timeAhead,
        address _acceptingThisToken
    ) {
        crowdfundingFactory = _crowdfundingFactory;
        goal = _goal;
        deadline = block.timestamp + _timeAhead;
        minimumContribution = 100;
        admin = msg.sender;
        token = IERC20(_acceptingThisToken);
        tokenAddress = _acceptingThisToken;
    }

    function contribute(uint amount, address caller) public returns (address) {
        // require(block.timestamp < deadline, "Deadline has passed");
        require(amount >= minimumContribution, "minimum contribution not met");

        require(
            token.allowance(caller, address(this)) >= amount,
            "token spend not approved"
        );

        if (contributers[caller] == 0) {
            unchecked {
                noOfContributors++;
            }
        }
        token.transferFrom(caller, address(this), amount);
        unchecked {
            contributers[caller] += amount;
            raisedAmount += amount;
        }

        return (caller);
        // emit ContributeEvent(msg.sender, amount);
    }

    function getBalance() public view returns (uint) {
        return token.balanceOf(address(this));
    }

    function getRefund(address caller) public returns (uint) {
        require(block.timestamp >= deadline && raisedAmount < goal);
        require(contributers[caller] > 0);

        uint amount = contributers[caller];
        contributers[caller] = 0;
        token.transfer(caller, amount);
        return amount;
        // emit RefundFromProject(address(this), caller, address(token), amount);
    }

    function createRequest(
        string memory _description,
        address _recipient,
        uint _amount
    ) public sharedOwner {
        Request storage newRequest = requests[numRequests];
        numRequests++;

        newRequest.description = _description;
        newRequest.recipient = _recipient;
        newRequest.amount = _amount;
        newRequest.completed = false;
        newRequest.noOfVoters = 0;

        emit CreateRequestEvent(_description, _recipient, _amount);
    }

    function voteRequest(uint _requestNo, address caller) public {
        require(numRequests >= _requestNo, "Request does not exist.");

        // owner of crowdfunding project cannot vote for their own requests
        require(caller != owner(), "You're not allowed to contribute");

        // must have contributed before voting
        require(contributers[caller] > 0, "You must be a contributor to vote!");

        Request storage thisRequest = requests[_requestNo];
        require(thisRequest.voters[caller] == false, "You have already voted");
        require(
            thisRequest.recipient != address(0),
            "Request has not been initiated"
        );

        // ratio of the value of contribution by one user to the raisedAmount
        // the higher the contribute the more value msg.sender contributes
        // solidity that no float or double
        thisRequest.valueOfVotes += (contributers[caller] * decimals) / goal;
        thisRequest.noOfVoters++;
        thisRequest.voters[caller] = true;
    }

    function receiveContribution(
        uint _requestNo
    ) public sharedOwner returns (address, uint) {
        require(
            raisedAmount >= goal,
            "raisedAmount must be more than or equal to goal"
        );

        Request storage thisRequest = requests[_requestNo];
        require(
            thisRequest.completed == false,
            "The request has been completed."
        );

        uint8 numerator = 5;
        uint8 denominator = 10;
        // value of votes more than 0.5 to pass
        require(
            thisRequest.valueOfVotes > (numerator * decimals) / denominator,
            "error"
        );

        token.transfer(thisRequest.recipient, uint(thisRequest.amount));
        thisRequest.completed = true;

        emit ReceiveContributionEvent(
            thisRequest.recipient,
            thisRequest.amount
        );
        return (thisRequest.recipient, thisRequest.amount);
    }

    function getRequestDescription(
        uint _requestNo
    ) external view returns (string memory) {
        return requests[_requestNo].description;
    }

    function getRequestRecipient(
        uint _requestNo
    ) external view returns (address) {
        return requests[_requestNo].recipient;
    }

    function getRequestAmount(uint _requestNo) external view returns (uint) {
        return requests[_requestNo].amount;
    }

    function getRequestCompleted(uint _requestNo) external view returns (bool) {
        return requests[_requestNo].completed;
    }

    function getRequestNumOfVoters(
        uint _requestNo
    ) external view returns (uint) {
        return requests[_requestNo].noOfVoters;
    }

    function getRequestValueOfVotes(
        uint _requestNo
    ) external view returns (uint) {
        return requests[_requestNo].valueOfVotes;
    }
}
